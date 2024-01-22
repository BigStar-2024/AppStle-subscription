
let filterObject = {};

const setFilterObject = (obj) => {
    filterObject = obj;
}
const getFilterObject = () => {
    return filterObject;
}
export default {
    setFilterObject,
    getFilterObject
}