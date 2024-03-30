import { createContext } from "react";

const OptionsContext = createContext({
    grantTypes: [],
    scopes: [],
    roles: []
});

export default OptionsContext