// Scenario: You have a backend response for a single book:
//     const book = { id: 101, title: "Spring Boot", author: "Pillai" };

// Your Task:

// Use Destructuring to get the title and author out of that book object.

// Use a Template Literal(Backticks) to create a sentence: "The book Spring Boot is written by Pillai."
const book = { id: 101, title: "Spring Boot", author: "Pillai" };
const { title, author } = book;

console.log(`the book ${book.title} is written by ${book.author}`)
console.log(`the book ${title} is written by ${author}`)

