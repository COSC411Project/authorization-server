import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './components/App'
import './index.css'
import OptionsContextProvider from './stores/OptionsContextProvider'

ReactDOM.createRoot(document.getElementById('root')).render(
    <OptionsContextProvider>
        <App />
    </OptionsContextProvider>,
)
