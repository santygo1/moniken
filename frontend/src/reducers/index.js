import { combineReducers } from "redux";
import collectionReducer from "./collectionReducer";

const rootReducer = combineReducers({
  collectionReducer,
});

export default rootReducer;
