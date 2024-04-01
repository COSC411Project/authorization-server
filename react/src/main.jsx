import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './components/App'
import './index.css'
import OptionsContextProvider from './stores/OptionsContextProvider'
import DataContextProvider from './stores/DataContextProvider'

ReactDOM.createRoot(document.getElementById('root')).render(
    <DataContextProvider>
        <OptionsContextProvider>
            <App />
        </OptionsContextProvider>
    </DataContextProvider>,
)
