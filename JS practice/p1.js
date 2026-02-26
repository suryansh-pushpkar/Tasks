// Question 1: The API Response Unpacker
// You receive a JSON response from your Spring Boot backend representing a User:
// { id: 101, name: "Suryansh", role: "Developer", stats: { loginCount: 5, lastLogin: "2026-02-24" } }

// Task: Use Destructuring to extract name, role, and the nested loginCount into three separate variables in just two lines of code.

const data = { id: 101, name: "Suryansh", role: "Developer", stats: { loginCount: 5, lastLogin: "2026-02-24" } }

const { name, role, stats: { loginCount } } = data;
const data2 = { name, role, loginCount };
console.log(data2)