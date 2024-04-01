import DataContext from '../stores/DataContext'
import Mapper from '../utils/Mapper';
import { useState, useEffect } from 'react';

const DataContextProvider = ({children}) => {
    const [clients, setClients] = useState([]);
    const [users, setUsers] = useState([]);

    useEffect(() => {
        const getClients = async () => {
            const response = await fetch("/api/clients");
            if (response.status >= 200 && response.status < 400) {
                const message = await response.json();
                const savedClients = message.map(clientDTO => Mapper.mapToClient(clientDTO));
                setClients(savedClients);
            }
        }

        getClients();
    }, [])

    const registerClient = async (clientRegistrationDTO) => {
        const response = await fetch("http://localhost:9000/api/clients/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(clientRegistrationDTO)
        });
        
        const message =  await response.json();
        if (response.status >= 200 && response.status < 400) {
            const secret = message.secret;

            const savedClient = Mapper.mapToClient(message);
            setClients(prevClients => prevClients.concat([savedClient]));

            return [true, {secret: secret, identifier: savedClient.identifier}];
        }

        return [false, message];
    }

    const data = {
        clients: clients,
        registerClient: registerClient,

        users: users
    };

    return (
        <DataContext.Provider value={data}>
            {children}
        </DataContext.Provider>
    )
}

export default DataContextProvider