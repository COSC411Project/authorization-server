import { createContext } from "react";

const DataContext = createContext({
    clients: [],
    registerClient: () => {},
    
    users: []
})

export default DataContext