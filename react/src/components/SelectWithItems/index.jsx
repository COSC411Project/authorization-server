
const SelectWithItems = ({id, display, items, value, onChange}) => {
    return (
        <select id={id} name={id} value={value} onChange={onChange}>
            <option hidden>{display}</option>
            {items.map(item => (
                <option key={item} value={item}>{item}</option>
            ))}
        </select>
    )
}

export default SelectWithItems