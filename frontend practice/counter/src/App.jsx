import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'

function App() {
let [counter, setCounter] = useState(0)

  const addValue = () =>{
    setCounter(counter+1)
  }


const removeValue = () =>{
    if(counter > 0){
    setCounter(counter -1);
    }
  
  console.log(counter)
}

  return (
    <><h1>React</h1>
    <h2>Counter value: {counter}</h2>
    <button
    onClick={addValue}
    >Add value: {counter}</button>
    <br />
    <button
    onClick={removeValue}
    >Remove Value: {counter}</button>

         </> 
  )
}

export default App
 