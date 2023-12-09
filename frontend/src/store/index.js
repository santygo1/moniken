import getCollectionsMiddleware from "../middleware/collection/getCollectionsMiddleware";
import { applyMiddleware, createStore } from "redux";
import { composeWithDevTools } from "redux-devtools-extension";
import rootReducer from "../reducers";
import addNewCollectionMiddleware from "../middleware/collection/addNewCollectionMiddleware";
import deleteCollectionMiddleware from "../middleware/collection/deleteCollectionMiddleware";
import updateCollectionMiddleware from "../middleware/collection/updateCollectionMiddleware";

export default function configureStore(preloadedState) {
  const middlewares = [
    getCollectionsMiddleware,
    addNewCollectionMiddleware,
    deleteCollectionMiddleware,
    updateCollectionMiddleware,
  ];
  const middlewareEnhancer = applyMiddleware(...middlewares);

  const enhancers = [middlewareEnhancer];
  const composedEnhancers = composeWithDevTools(...enhancers);

  const store = createStore(rootReducer, preloadedState, composedEnhancers);

  return store;
}
