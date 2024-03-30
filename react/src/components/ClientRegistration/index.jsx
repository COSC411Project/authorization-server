import GrantType from '../../models/GrantType'
import OptionsContext from '../../stores/OptionsContext'
import Scope from '../../models/Scope'
import SelectWithItems from '../SelectWithItems'
import RedirectUri from '../../models/RedirectUri'
import style from './style.module.css'
import { useContext, useState, useCallback } from 'react'
import { v4 } from 'uuid'

const ClientRegistration = () => {
    const [applicationName, setApplicationName] = useState("");
    const [grantType, setGrantType] = useState("");
    const [scopeRequired, setScopeRequired] = useState(false);
    const [scope, setScope] = useState("");
    const [redirectUris, setRedirectUris] = useState([new RedirectUri(v4(), "")]);
    const optionsContext = useContext(OptionsContext);

    const handleApplicationNameChange = (event) => {
        setApplicationName(event.target.value);
    }

    const handleGrantTypeChange = (event) => {
        setGrantType(event.target.value);
    }

    const handleScopeRequiredChange = (event) => {
        if (event.target.value === "yes") {
            setScopeRequired(true);
        } else {
            setScopeRequired(false);
        }
    }

    const handleScopeChange = (event) => {
        setScope(event.target.value);
    }
    
    const handleRegister = async (event) => {
        event.preventDefault();
        
        const dto = {
            "applicationName": applicationName,
            "grantTypes": grantTypes,
            "scopeRequired": scopeRequired,
            "scopes": scopes,
            "redirectUris": redirectUris.filter(redirectUri => redirectUri.value.length > 0)
                                        .map(redirectUri => redirectUri.value)
        }
    
        const response = await fetch("http://localhost:9000/api/clients/register", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(dto)
        });
        
        if (response.status === 201) {

        }
    }

    return (
        <section className={style.container}>
            <form onSubmit={handleRegister}>
                <div>
                    <label htmlFor="name">Application name:</label>
                    <input 
                        id="name" 
                        type="text" 
                        onChange={handleApplicationNameChange} 
                        value={applicationName}
                    />
                </div>
                
                <div>
                    <label htmlFor="grantType">Grant Type:</label>
                    <SelectWithItems 
                        id="grantType"
                        display="Select a grant type"
                        items={optionsContext.grantTypes}
                        onChange={handleGrantTypeChange}
                        value={grantType}
                    />
                </div>

                <div className={style.scopeRequired}>
                    <label htmlFor="scope">Scope Required:</label>
                    <div>
                        <label htmlFor="yes">
                            <input 
                                type="radio" 
                                name="scope" 
                                id="yes" 
                                value="yes" 
                                checked={scopeRequired === true}
                                onChange={handleScopeRequiredChange}
                            />
                            Yes
                        </label>
                        <label htmlFor="no">
                            <input 
                                type="radio" 
                                name="scope" 
                                id="no" 
                                value="no" 
                                checked={scopeRequired === false}
                                onChange={handleScopeRequiredChange}
                            />
                            No
                        </label>
                    </div>
                </div>

                <div>
                    <label htmlFor='scope'>Scope:</label>
                    <SelectWithItems
                        id="scope"
                        display="Select a scope"
                        items={optionsContext.scopes}
                        value={scope}
                        onChange={handleScopeChange}
                    />
                </div>

                <div className={style.redirectUris}>
                    <label>Redirect Uri(s):</label>
                    <ModifiableInputList 
                        items={redirectUris}
                        setter={setRedirectUris}
                        Model={RedirectUri}
                    />
                </div>

                <div className={style.register}>
                    <input type="submit" value="Register" />
                </div>
            </form>
        </section>
    )
}

const ModifiableInputList = ({items, setter, Model}) => {
    const handleChange = useCallback((event) => {
        const target = event.target;
        const uuid = target.getAttribute("data-key");
        const index = items.findIndex(item => item.key === uuid);
        const clone = items[index].clone();
        clone.value = target.value;

        setter(prevItems => prevItems.toSpliced(index, 1, clone));
    }, [items]);

    const handleButtonClick = (event) => {
        event.preventDefault();

        const target = event.target;
        const index = target.getAttribute("data-index");
        if (target.value === "+") {
            setter(prevItems => prevItems.concat([new Model(v4(), "")]));
        } else {
            setter(prevItems => prevItems.toSpliced(index, 1));
        }
    }

    return (
        <ul>
            {items.map((item, index) => (
                <li key={item.key}>
                    <input 
                        type="text" 
                        data-key={item.key}
                        value={item.value}
                        onChange={handleChange}
                    />

                    <button value={index === items.length - 1 ? "+" : "-"} 
                            onClick={handleButtonClick} 
                            data-key={item.key}>
                        {index === items.length - 1 ? "+" : "-"}
                    </button>
                </li>
            ))}
        </ul>
    )
}

export default ClientRegistration