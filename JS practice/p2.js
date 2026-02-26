// Question 2: The Library Formatter
// You have an array of book objects:
// [{ id: 1, title: "Java" }, { id: 2, title: "React" }]

// Task: Use.map() and Template Literals to create a new array of strings that looks like this: ["ID: 1 - Title: JAVA", "ID: 2 - Title: REACT"]. (Hint: Use.toUpperCase() inside your template literal).

const books = [{ id: 1, title: "Java" }, { id: 2, title: "React" }];

const formatBook = books.map(book => `ID: ${book.id} - Title: ${book.title.toUpperCase()}`);
console.log(formatBook)

