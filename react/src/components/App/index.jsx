import { BrowserRouter, Routes, Route, Outlet } from 'react-router-dom'
import Client from '../Client'
import Clients from '../Clients'
import Header from '../Header'
import Users from '../Users'
import ClientRegistration from '../ClientRegistration'

const App = () => {
    return (
        <BrowserRouter>
            <Header />

            <main>
                <Routes>
                    <Route path="/dev/clients">
                        <Route index element={<Clients />} />
                        <Route path="register" element={<ClientRegistration />} />
                        <Route path=":id" element={<Client />} />
                    </Route>
                    <Route path="/dev/users" element={<Users />} />
                </Routes>
            </main>
        </BrowserRouter>
    )
}

export default App