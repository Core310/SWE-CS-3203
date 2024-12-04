const puppeteer = require("puppeteer");
const { MongoClient } = require("mongodb");
require('dotenv').config({ path: require('path').resolve(__dirname, '../secrets.env') });

(async () => {
  const url = "https://classnav.ou.edu/#semester/202420/subject_code/C%20S";
  const browser = await puppeteer.launch({ headless: true });
  const page = await browser.newPage();

  await page.goto(url, { waitUntil: "domcontentloaded" });

  // Wait for the table to load
  await page.waitForSelector("tbody tr");

  // Function to extract data from rows
  const extractData = async () => {
    return await page.evaluate(() => {
      const rows = Array.from(document.querySelectorAll("tbody tr"));
      return rows.map(row => {
        return {
          class: row.className.trim(),
          data: Array.from(row.querySelectorAll("td")).map(cell => cell.textContent.trim()), // Extract cell data
        };
      });
    });
  };

  // Extract data from the first page
  let allData = await extractData();

  // Loop through the first 8 pages
  for (let pageNum = 1; pageNum <= 8; pageNum++) {
    if (pageNum > 1) {
      // Click the "Next" button to go to the next page
      await page.click(".next");  // Adjust the selector if necessary
      await page.waitForSelector("tbody tr");  // Wait for rows to load
    }

    // Extract the data from the current page
    const pageData = await extractData();
    allData = [...allData, ...pageData];
  }

  // Process and insert data into MongoDB
const rowData = allData.map(row => ({
  CRN: row.data[0],
  subject: row.data[1],
  course: row.data[2],
  sectionTitle: row.data[3],
  primaryInstructor: row.data[4],
  courseDates: row.data[5],
  seatsLeft: row.data[6],
  waitList: row.data[7],
  // Add more mappings as needed
}));
  const uri = `mongodb+srv://${process.env.MONGO_USER}:${process.env.MONGO_PASSWORD}@${process.env.MONGO_URI}`;
  const client = new MongoClient(uri, { useNewUrlParser: true, useUnifiedTopology: true });

  try {
    await client.connect();
    const database = client.db("SWE");
    const collection = database.collection("classData");

    const result = await collection.insertMany(rowData);
    console.log(`${result.insertedCount} documents were inserted`);
  } finally {
    await client.close();
  }

  await browser.close();
})();
