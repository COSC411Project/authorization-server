import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { useState } from 'react'
import Login from '../Login'

function App() {
  return (
    <BrowserRouter>
      <main>
        <Routes>
          <Route path='/login' element={<Login />} />
        </Routes>
      </main>
    </BrowserRouter>
  )
}

export default App
