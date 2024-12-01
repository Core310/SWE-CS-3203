import csv
import json
import re

from class2 import main

from second_parser import parse_data as fallback_parse_data  # Assuming second_parser contains fallback logic

def parse_data(lines):
    parsed_data = []
    current_record = {}
    prev_line = None  # Store the previous line to capture course name
    # Regex to validate course dates format (e.g., "Aug 19 - Dec 6")
    course_dates_pattern = r"^\w+ \d+ - \w+ \d+$"

    line_iterator = iter(lines)

    try:
        for line in line_iterator:
            line = line.replace("\\n", " ").strip()

            # Skip lines containing "[" or "]" directly
            if "[" in line or "]" in line:
                continue

            # Skip "Seats" line and the next 5 lines if "Seats" is encountered
            if line.startswith("Seats"):
                for _ in range(5):
                    next(line_iterator, None)
                continue

            # If the previous line is not None and the current line contains "INSTRUCTOR"
            if "INSTRUCTOR" in line and prev_line:
                current_record["course name"] = prev_line  # Capture course name from previous line
                print(f"Found Course Name: {prev_line}")  # Debug output

                instructor_part = line.split("INSTRUCTOR", 1)[1]
                instructor_name = instructor_part.split("CLASS DESCRIPTION", 1)[0].strip() if "CLASS DESCRIPTION" in instructor_part else instructor_part.strip()
                current_record["instructor"] = instructor_name.replace("\\n", " ").strip()
                print(f"Extracted Instructor: {instructor_name}")  # Debug output

            # Store the current line as the previous line for the next iteration
            prev_line = line

            # Extract course description if "CLASS DESCRIPTION" is in the line
            if "CLASS DESCRIPTION" in line:
                description_part = line.split("CLASS DESCRIPTION", 1)[1]
                description_text = description_part.split("QUICK FACTS", 1)[0].strip() if "QUICK FACTS" in description_part else description_part.strip()
                current_record["course description"] = description_text.replace("\\n", " ").strip()

            # Extract seats left if "Seats Left:" appears in the line
            if "Seats Left:" in line:
                current_record["seats left"] = line.split("Seats Left:")[1].split("Schedule:")[0].strip()

            # Extract schedule if "Schedule:" appears in the line
            if "Schedule:" in line:
                current_record["Schedule"] = line.split("Schedule:")[1].split("Delivery:")[0].strip()

            # Extract delivery type if "Delivery:" appears in the line
            if "Delivery:" in line:
                current_record["delivery"] = line.split("Delivery:")[1].split("Term")[0].strip().replace("\\n", " ").strip()

            # Extract repeatable status if "Repeatable:" appears in the line
            if "Repeatable:" in line:
                current_record["repeatable"] = line.split("Repeatable:")[1].split("MEETING")[0].strip().replace("\\n", " ").strip()

            # Extract general education type if "GenEd Type:" appears in the line
            if "GenEd Type:" in line:
                current_record["Gened type"] = line.split("GenEd Type:")[1].split("Wait List")[0].strip().replace("\\n", " ").strip()

            # Extract waitlist status if "Wait List:" appears in the line
            if "Wait List:" in line:
                current_record["Waitlist"] = line.split("Wait List:")[1].split("Repeatable")[0].strip().replace("\\n", " ").strip()

            if "MEETING DAYS" in line:
                # Extract everything after "MEETING DAYS"
                meeting_info = line.split("MEETING DAYS")[1].strip()

                # Split by "(Final Exam)" if it exists to separate regular meeting info from final exam details
                if "(Final Exam)" in meeting_info:
                    regular_meeting, info = meeting_info.split("(Final Exam)")
                    parts = info.strip().split()

                    # Extract final exam date, time, and location from parts
                    if len(parts) >= 6:
                        #current_record["course dates"] = f"{parts[0]} {parts[1]}"
                        course_dates = f"{parts[0]} {parts[1]}"

                        if re.match(course_dates_pattern, course_dates):
                            current_record["course dates"] = course_dates
                            current_record["meeting times"] = f"{parts[2]} {parts[3]} - {parts[4]} {parts[5]}"

                            # Join remaining parts to form the location string
                            location = " ".join(parts[6:])

                            # Check for meeting days within the final exam location and remove them
                            if "MWF" in location:
                                current_record["meeting days"] = "MWF"
                                location = location.replace("MWF", "").strip()
                            elif "TR" in location:
                                current_record["meeting days"] = "TR"
                                location = location.replace("TR", "").strip()
                            elif "MTWRF" in location:
                                current_record["meeting days"] = "MTWTRF"
                                location = location.replace("MTWTRF", "").strip()
                            elif "M" in location:
                                current_record["meeting days"] = "M"
                                location = location.replace("M", "").strip()
                            elif "T" in location:
                                current_record["meeting days"] = "T"
                                location = location.replace("T", "").strip()
                            elif "W" in location:
                                current_record["meeting days"] = "W"
                                location = location.replace("W", "").strip()
                            elif "F" in location:
                                current_record["meeting days"] = "F"
                                location = location.replace("F", "").strip()
                            else:
                                current_record["meeting days"] = "Days not specified"

                            # Remove any trailing newline or unwanted text (e.g., "\nDec")
                            if "\n" in location:
                                location = location.split("\n")[0].strip()
                            current_record["location"] = location
                            print(location)

                        else:
                                # Regex patterns
                            meeting_pattern = (
                                r"(?P<dates>\w+ \d+ - \w+ \d+) "  # Match course dates
                                r"(?P<times>\d+:\d+ [apm]+ - \d+:\d+ [apm]+) "  # Match meeting times
                                r"(?P<location>.+?) "  # Match location
                                r"(?P<days>[MTWRF]+)"  # Match meeting days
                            )
                            final_exam_pattern = (
                                r"(?P<date>\w+ \d+) "  # Match final exam date
                                r"(?P<start_time>\d+:\d+ [apm]+) - (?P<end_time>\d+:\d+ [apm]+) "  # Match final exam time range
                                r"(?P<location>.+)"  # Match final exam location
                            )

  
                            match = re.search(meeting_pattern, meeting_info)
                            if match:
                                current_record["course dates"] = match.group("dates")
                                current_record["meeting times"] = match.group("times")
                                current_record["location"] = match.group("location")
                                current_record["meeting days"] = match.group("days")
                            else:
                                # Default values for missing fields
                                current_record["course dates"] = "Dates not available"
                                current_record["meeting times"] = "Times not available"
                                current_record["location"] = "Location not available"
                                current_record["meeting days"] = "Days not available"

                            # Extract final exam information
                            if "(Final Exam)" in meeting_info:
                                # Split into regular meeting info and final exam details
                                regular_meeting, final_exam_info = meeting_info.split("(Final Exam)")
                                parts = final_exam_info.strip().split()

                                # Extract the date directly by indexing 5 characters before "(Final Exam)"
                                date_index = meeting_info.find("(Final Exam)") - 7  # Get the starting index of the date
                                current_record["final exam date"] = meeting_info[date_index:date_index + 7].strip().replace("n", "")

                                # Extract the final exam time and location
                                if len(parts) >= 4:  # Ensure we have enough data
                                    current_record["final exam time"] = " ".join(parts[:5])  # First two parts are the time range
                                    current_record["final exam location"] = " ".join(parts[5:])  # Remaining parts are the location
                                else:
                                    current_record["final exam time"] = "Final exam time not available"
                                    current_record["final exam location"] = "Final exam location not available"

                            # Check if the current record is complete before appending
                            if "course name" in current_record and "instructor" in current_record:
                                parsed_data.append(current_record)
                                current_record = {}  # Reset for the next record



                    else:
                        current_record["course dates"] = "Final exam date not available"
                        current_record["meeting times"] = "Final exam time not available"
                        current_record["location"] = "Final exam location not available"
                        current_record["meeting days"] = "Days not specified"


                else:
                    # If no final exam, the entire meeting info is regular meeting details
                    regular_meeting = meeting_info

                    # Now parse regular meeting details
                    parts = regular_meeting.strip().split()

                    # Default values for missing fields
                    current_record["course dates"] = "Dates not available"
                    current_record["meeting times"] = "Times not available"
                    current_record["location"] = "Location not available"
                    current_record["meeting days"] = "Days not available"

                    # Check if meeting info explicitly mentions missing times/locations
                    if "Times not available" in meeting_info:
                        current_record["meeting times"] = "Times not available"
                        current_record["course dates"] = f"{parts[0]} {parts[1]} - {parts[3]} {parts[4]}"
                    if "Location not available" in meeting_info:
                        current_record["location"] = "Location not available"

                    if "Times not available" not in meeting_info or "Location not available" not in meeting_info:
    
                        # Ensure we have enough parts for course dates and times
                        if len(parts) >= 5:
                            current_record["course dates"] = f"{parts[0]} {parts[1]} - {parts[3]} {parts[4]}"
                        else:
                            current_record["course dates"] = "Dates not available"
    
                        if len(parts) >= 10:
                            current_record["meeting times"] = f"{parts[5]} {parts[6]} - {parts[8]} {parts[9]}"
                        else:
                            current_record["meeting times"] = "Times not available"

                        if len(parts) >= 11:
                            # Clean location and meeting days
                            location_parts = " ".join(parts[10:-1]).replace("\n", "").replace(",", "").strip()
                            meeting_days = parts[-1].replace("\n", "").replace(",", "").strip()
        
                            # If location contains "MWF" or "TR" as part of the text, separate it to meeting days
                            if "MWF" in location_parts:
                                current_record["meeting days"] = "MWF"
                                location_parts = location_parts.replace("MWF", "").strip()
                                location_parts.split("\n")[0].strip()
                        
                            elif "TR" in location_parts:
                                current_record["meeting days"] = "TR"
                                location_parts = location_parts.replace("TR", "").strip()
                                location_parts.split("\n")[0].strip()
                            else:
                                current_record["meeting days"] = meeting_days if meeting_days else "Days not available"
                                location_parts.split("\n")[0].strip()
                            current_record["location"] = location_parts if location_parts else "Location not available"
                            location_parts.split("\n")[0].strip()
                            #print (location)

                        else:
                            current_record["location"] = "Location not available"
                            current_record["meeting days"] = "Days not available"
                



                if "(Final Exam)" in meeting_info:
                    # Split into regular meeting info and final exam details
                    regular_meeting, final_exam_info = meeting_info.split("(Final Exam)")
                    parts = final_exam_info.strip().split()

                    # Extract the date directly by indexing 5 characters before "(Final Exam)"
                    date_index = meeting_info.find("(Final Exam)") - 7  # Get the starting index of the date
                    current_record["final exam date"] = meeting_info[date_index:date_index + 7].strip().replace("n", "")

                    # Extract the final exam time and location
                    if len(parts) >= 4:  # Ensure we have enough data
                        current_record["final exam time"] = " ".join(parts[:5])  # First two parts are the time range
                        current_record["final exam location"] = " ".join(parts[5:])  # Remaining parts are the location
                    else:
                        current_record["final exam time"] = "Final exam time not available"
                        current_record["final exam location"] = "Final exam location not available"


            

            # Check if the current record is complete before appending
            if "course name" in current_record and "instructor" in current_record:
                print(f"Adding Record: {current_record}")  # Debug output
                parsed_data.append(current_record)
                current_record = {}  # Reset for the next record

    except StopIteration:
        print("Reached the end of data unexpectedly. Some data may be incomplete.")

    return parsed_data



def write_to_csv(parsed_data, output_filename="courses3.csv"):
    headers = ["course name", "instructor", "course description", "course dates", "seats left", "Schedule", "delivery", "repeatable", "Gened type", "Waitlist", "meeting times", "location", "meeting days", "final exam time", "final exam date", "final exam location"]
    
    with open(output_filename, mode="w", newline="", encoding="utf-8") as csvfile:
        writer = csv.DictWriter(csvfile, fieldnames=headers)
        writer.writeheader()
        for record in parsed_data:
            writer.writerow(record)

def main():
    input_filename = "sample.json"  # Replace with the name of your input file
    with open(input_filename, "r", encoding="utf-8") as file:
        lines = file.readlines()
    
    parsed_data = parse_data(lines)
    write_to_csv(parsed_data)

if __name__ == "__main__":
    main()
