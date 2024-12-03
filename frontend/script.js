// Attach event listener to the form submission
document.getElementById('courseSearchForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent default form submission behavior
  
    alert("Form submitted!"); // Debugging: Check if form submission is detected

    // Get user input values from the form
    const classId = document.getElementById('classId').value;
    const semesterYear = document.getElementById('semesterYear').value;
  
    alert(`Class ID: ${classId}, Semester: ${semesterYear}`); // Debugging: Check input values

    // Mock data to simulate API response
    const mockData = [
      { Course: "CS101", Section: "A", Instructor: "Dr. Smith", Seats: 20, Waitlist: 5 },
      { Course: "CS102", Section: "B", Instructor: "Prof. Johnson", Seats: 25, Waitlist: 2 }
    ];

    /* Once the backend API becomes functional, replace the mock data with
     an actual API call. Update the fetch function to call the backend endpoint */
    

    // fetch(`/api/search?classId=${classId}&semesterYear=${semesterYear}`)
    //     .then(response => response.json())
    //     .then(data => {
    //         // Use data from the backend instead of mock data
    //         populateTable(data);
    //     })
    //     .catch(error => {
    //         console.error('Error:', error); // Log errors to the console
    //     });

    try{
    // Simulate a fetch response using mock data
    const filteredData = mockData.filter(course => 
      course.Course.toLowerCase().includes(classId.toLowerCase())
    );
    alert(`Filtered Results: ${JSON.stringify(filteredData)}`); // Debugging: Check the filtered data
  
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
      alert(`Adding row: ${JSON.stringify(course)}`); // Debugging: Check each course being added
      const row = tbody.insertRow();
      row.insertCell(0).textContent = course.Course; // Add Course value
      row.insertCell(1).textContent = course.Section; // Add Section value
      row.insertCell(2).textContent = course.Instructor; // Add Instructor value
      row.insertCell(3).textContent = course.Seats; // Add Seats value
      row.insertCell(4).textContent = course.Waitlist; // Add Waitlist value
    });
    } catch (error) {
        // Error handler
        alert(`Error occurred: ${error.message}`); // Display an alert for the error
        console.error('Error:', error); // Log the error to the console for debugging
    }
  });
  