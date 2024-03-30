import { Link } from "react-router-dom"
import style from './style.module.css'

const Header = () => {

    return (
        <header className={style.container}>
            <nav>
                <ul>
                    <li><Link to="/dev/users">Users</Link></li>
                    <li><Link to="/dev/clients">Clients</Link></li>
                </ul>
            </nav>
        </header>
    )
}

export default Header