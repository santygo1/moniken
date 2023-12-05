import { combineReducers } from "redux";
import collectionReducer from "./collectionReducer";
import routesReducer from "./routesReducer";

const rootReducer = combineReducers({
  collectionReducer,
  routesReducer,
});

export default rootReducer;
