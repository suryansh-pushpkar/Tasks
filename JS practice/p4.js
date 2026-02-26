// Scenario: You are building a search feature for your Library Management System.You have an array of book objects:

// JavaScript
// const myBooks = [
//     { id: 1, title: "Java Core", price: 400 },
//     { id: 2, title: "Spring Microservices", price: 800 },
//     { id: 3, title: "React Basics", price: 600 }
// ];
// Your Task:

// Use.map() on the myBooks array.

// Inside the.map(), use Destructuring to get the title and price.

// Return a Template Literal string for each book that says: "Book: [TITLE] costs [PRICE] rupees."
const myBooks = [
    { id: 1, title: "Java Core", price: 400 },
    { id: 2, title: "Spring Microservices", price: 800 },
    { id: 3, title: "React Basics", price: 600 }
];


const info = myBooks.filter(book => book.price > 500).map(({ title, price }) => {
    return `Title ${title} Price ${price}`;
}
)
console.log(info)