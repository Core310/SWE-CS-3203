// Attach event listener to the form submission
document.getElementById('courseSearchForm').addEventListener('submit', function(event) {
  event.preventDefault(); // Prevent default form submission behavior

  // Get user input values from the form
  const classId = document.getElementById('classId').value;
  const semesterYear = document.getElementById('semesterYear').value;

  // Debugging: Check input values
  alert(`Class ID: ${classId}, Semester: ${semesterYear}`);

  // Determine which CSV file to load based on the selected semester
  let csvFile = '';
  if (semesterYear === 'Fall 2024') {
    csvFile = 'Fall 2024 - all classes cleaned.csv';
  } else if (semesterYear === 'Spring 2025') {
    csvFile = 'spring 2025 - cleaned.csv';
  } else if (semesterYear === 'Summer 2025') {
    csvFile = 'summer 2025 - cleaned.csv';
  }

  // Fetch the corresponding CSV data from a local or remote CSV file
  fetch(csvFile)
    .then(response => response.text()) // Get the text from the CSV file
    .then(csvData => {
      // Parse the CSV data using PapaParse
      const parsedData = Papa.parse(csvData, {
        header: true, // Use the first row as header
        skipEmptyLines: true, // Skip empty lines
      });

      // Filter the data based on the class ID
      const filteredData = parsedData.data.filter(course => 
        course['course name'].toLowerCase().includes(classId.toLowerCase())
      );

      // Debugging: Check the filtered data
      alert(`Filtered Results: ${JSON.stringify(filteredData)}`);

      // Get the table body element to populate results
      const tbody = document.getElementById('resultsTable').getElementsByTagName('tbody')[0];
      tbody.innerHTML = ''; // Clear previous results

      // If no data is found, show a message
      if (filteredData.length === 0) {
        const row = tbody.insertRow();
        const cell = row.insertCell(0);
        cell.colSpan = 5; // Make the message span across all columns
        cell.textContent = "No results found.";
        cell.className = "text-center text-muted"; // Add a muted style
        return;
      }

      // Populate the table with filtered data
      filteredData.forEach(course => {
        const row = tbody.insertRow();
        row.insertCell(0).textContent = course['course name']; // Add Course name
        row.insertCell(1).textContent = course['Schedule']; // Add Schedule (or Section)
        row.insertCell(2).textContent = course['instructor']; // Add Instructor
        row.insertCell(3).textContent = course['seats left']; // Add Seats left
        row.insertCell(4).textContent = course['Waitlist']; // Add Waitlist information
      });
    })
    .catch(error => {
      console.error('Error:', error); // Log errors to the console
    });
});
