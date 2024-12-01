import os
import csv
import json
import time
from selenium import webdriver
from selenium.webdriver.support.ui import Select
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from urllib.parse import quote
from selenium.common.exceptions import StaleElementReferenceException, TimeoutException

# Initialize WebDriver
def initialize_webdriver(chrome_driver_path):
    chrome_service = Service(chrome_driver_path)
    options = Options()
    driver = webdriver.Chrome(service=chrome_service, options=options)
    return driver

# Navigate to a URL
def navigate_to_page(driver, url):
    driver.get(url)
    try:
        WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.ID, 'semester'))
        )
        WebDriverWait(driver, 5).until(
            EC.visibility_of_element_located((By.ID, 'semester'))
        )
    except TimeoutException:
        print("Timeout waiting for page to load or element to become visible.")

# Get all semester values
def get_semester_values(driver):
    semester_select_box = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.ID, 'semester'))
    )
    semester_select = Select(semester_select_box)
    return [option.get_attribute("value") for option in semester_select.options]

def wait_for_subject_dropdown(driver):
    subject_selectBox= WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.ID, 'subject'))
    )
    subject_select = Select(subject_selectBox)
    return subject_selectBox

# Get all subject values for a selected semester
def get_subject_values(driver):
    subject_select_box = WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.ID, 'subject'))
    )
    subject_select = Select(subject_select_box)
    return [option.get_attribute("value") for option in subject_select.options]

# Expand rows to see all data
def expand_rows(driver):
    plus_icons = driver.find_elements(By.CLASS_NAME, 'ui-icon-circle-plus')
    for icon in plus_icons:
        driver.execute_script("arguments[0].scrollIntoView(true);", icon)
        icon.click()
        time.sleep(1)

def click_next(driver):
    try:
        # Locate the "Next" button by ID
        next_icon = driver.find_element(By.ID, 'clist_next')
        
        # Check if "Next" button is enabled (not disabled)
        if "disabled" not in next_icon.get_attribute("class"):
            driver.execute_script("arguments[0].scrollIntoView(true);", next_icon)
            next_icon.click()
            time.sleep(1)  # Optional delay for page loading
            return True  # Successfully clicked 'Next'
        else:
            return False  # "Next" button is disabled, no more pages
    except:
        return False  # No "Next" button found


# Close expanded rows
def close_rows(driver):
    minus_icons = driver.find_elements(By.CLASS_NAME, 'ui-icon-circle-minus')
    for icon in minus_icons:
        driver.execute_script("arguments[0].scrollIntoView(true);", icon)
        icon.click()
        time.sleep(1)

# Extract table data
def extract_table_data(driver):
    WebDriverWait(driver, 10).until(
        EC.presence_of_element_located((By.XPATH, '//*[@id="clist"]/tbody'))
    )
    tbody = driver.find_element(By.XPATH, '//*[@id="clist"]/tbody')
    data = []

    # Extract each row's data, skipping empty rows
    for tr in tbody.find_elements(By.XPATH, './/td'):
        row = [td.text for td in tr.find_elements(By.XPATH, './/tr')]
        if any(cell.strip() for cell in row) and not row[0].startswith("Seats Left"):  # Check if row has non-empty cells
            data.append(row)

    return data


# Construct the URL for semester and subject
def construct_url(base_url, semester_value, subject_value):
    encoded_semester = quote(semester_value)
    encoded_subject = quote(subject_value)
    return f"{base_url}#semester/{encoded_semester}/subject_code/{encoded_subject}"

# Write data to a CSV file for the semester
def write_to_csv(csv_filename, subject_name, data):
    print(f"Debug: Preparing to write CSV: {csv_filename}")
    file_exists = os.path.exists(csv_filename)
    try:
        with open(csv_filename, "a", newline='', encoding='utf-8') as csvfile:
            writer = csv.writer(csvfile)
            if not file_exists:
                print(f"Debug: Writing header to new file {csv_filename}")
                writer.writerow(["Subject", "Data"])  # Write header if file is new
            for row in data:
                if row:
                    writer.writerow([subject_name] + (row if isinstance(row, list) else [row]))
    except Exception as e:
        print(f"Error writing to CSV {csv_filename}: {e}")


def write_to_json(json_filename, subject_name, data):
    print(f"Debug: Preparing to write JSON: {json_filename}")
    semester_data = {}
    if os.path.exists(json_filename):
        try:
            with open(json_filename, "r", encoding='utf-8') as jsonfile:
                semester_data = json.load(jsonfile)
        except json.JSONDecodeError:
            print(f"Warning: JSON file {json_filename} is empty or invalid. Starting fresh.")

    # Update the JSON structure
    semester_data[subject_name] = data
    try:
        with open(json_filename, "w", encoding='utf-8') as jsonfile:
            json.dump(semester_data, jsonfile, indent=4, ensure_ascii=False)
    except Exception as e:
        print(f"Error writing to JSON {json_filename}: {e}")


# Main function
# Global list to store subject values
subject_values = []

def main():
    global subject_values  # Access the global subject_values variable
    global csv_filename
    global json_filename
    chrome_driver_path = 'C:/Users/masiko/Downloads/chromedriver-win64 (1)/chromedriver-win64/chromedriver.exe'
    base_url = "https://classnav.ou.edu/"

    with open("classproject.csv", "w", newline='', encoding='utf-8') as csvfile, open("sample.json", "w") as jsonfile:
        writer = csv.writer(csvfile)

    # Get semester values using a temporary driver
    driver = initialize_webdriver(chrome_driver_path)
    try:
        navigate_to_page(driver, base_url)
        semester_values = get_semester_values(driver)
        print("Semester Values:", semester_values)

        for semester_value in semester_values:
            # Ensure dropdown is loaded
            semester_select_box = WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.ID, 'semester'))
            )
            semester_select = Select(semester_select_box)
            semester_select.select_by_value(semester_value)
            wait_for_subject_dropdown(driver)

            # Get semester name
            semester_name = next(
                (option.text.strip()
                 for option in semester_select.options
                 if option.get_attribute("value") == semester_value),
                "Unknown Semester"
            )

            print(semester_name)

            # File names
            csv_filename = f"{semester_value}.csv"
            json_filename = f"{semester_value}.json"

            print(f"Processing Semester: {semester_name} (Files: {csv_filename}, {json_filename})")

            # Get subject values and assign them to the global variable
            subject_values = get_subject_values(driver)
            print(f"Subject Values for Semester {semester_value}:", subject_values)
            print(semester_name)
            print(subject_values)

    except Exception as e:
        print(f"Error during processing: {e}")
    finally:
        driver.quit()

    for semester_value in semester_values:
        if semester_value == "test":
            continue

        csv_filename = f"{semester_value}.csv"
        json_filename = f"{semester_value}.json"

        driver = initialize_webdriver(chrome_driver_path)

        with open(csv_filename, "w", newline='', encoding='utf-8') as csvfile, open(json_filename, "w") as jsonfile:
            writer = csv.writer(csvfile)

        subject_counter = 0

        try:
            for subject_value in subject_values:
                if subject_value == "" or subject_value == "all":
                    continue  # Skip invalid or "all" subjects

                retries = 0  # Initialize retry counter
                retry_limit = 1  # Allow one retry before skipping

                while retries <= retry_limit:
                    try:
                        # Restart WebDriver every 2 subjects
                        if subject_counter > 0 and subject_counter % 2 == 0:
                            driver.quit()
                            driver = initialize_webdriver(chrome_driver_path)
                            time.sleep(2)

                        subject_counter += 1

                        # Construct and visit URL
                        new_url = construct_url(base_url, semester_value, subject_value)
                        print(f"Attempting to visit URL: {new_url}")
                        navigate_to_page(driver, new_url)

                        time.sleep(2)

                        # Extract data
                        expand_rows(driver)
                        data = extract_table_data(driver)
                        print(f"Successfully extracted data for {subject_value}.")

                        # Write to semester-specific files
                        write_to_csv(csv_filename, subject_value, data)
                        write_to_json(json_filename, subject_value, data)

                        time.sleep(2)
                        # Close rows and wait before moving to the next subject
                        close_rows(driver)
                        time.sleep(1)

                        # Handle pagination with `click_next`
                        while click_next(driver):  # Keep clicking 'Next' until the end
                            expand_rows(driver)
                            data = extract_table_data(driver)
                            write_to_csv(csv_filename, subject_value, data)
                            write_to_json(json_filename, subject_value, data)
                            close_rows(driver)
                            time.sleep(3)  # Optional delay for viewing

                        break  # Exit the retry loop if successful

                    except StaleElementReferenceException as e:
                        print(f"StaleElementReferenceException encountered for {subject_value}. Retrying... (Attempt {retries + 1}/{retry_limit + 1})")
                        retries += 1
                        driver.quit()  # Close the current driver
                        driver = initialize_webdriver(chrome_driver_path)  # Reinitialize the driver
                        time.sleep(2)

                    if retries > retry_limit:
                        print(f"Skipping subject {subject_value} after exceeding retry limit.")
                        break

        except Exception as e:
            print(f"Error processing semester {semester_value}: {e}")
        finally:
            driver.quit()  # Quit the driver after finishing with the semester


if __name__ == "__main__":
    main()

