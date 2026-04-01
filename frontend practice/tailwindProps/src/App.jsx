import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import Cards from './components/Cards'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
  <h1 className=' bg-green-400 text-black p-20 rounded m-auto w-2xl '>Hello Tailwind</h1>
  <Cards />
    </>
  )
}

export default App
