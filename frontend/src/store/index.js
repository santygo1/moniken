import getCollectionsMiddleware from "../middleware/collection/getCollectionsMiddleware";
import { applyMiddleware, createStore } from "redux";
import { composeWithDevTools } from "redux-devtools-extension";
import rootReducer from "../reducers";
import addNewCollectionMiddleware from "../middleware/collection/addNewCollectionMiddleware";
import deleteCollectionMiddleware from "../middleware/collection/deleteCollectionMiddleware";
import updateCollectionMiddleware from "../middleware/collection/updateCollectionMiddleware";
import {
  getRouteById,
  getRoutesByCollectionNameApi,
} from "../api/routes/routesApi";
import getRoutesByCollectionNameMiddleware from "../middleware/routes/getRoutesByCollectionNameMiddleware";
import deleteRouteByIdMiddleware from "../middleware/routes/deleteRouteByIdMiddleware";
import createNewRouteMiddleware from "../middleware/routes/createNewRouteMiddleware";
import getRouteByIdMiddleware from "../middleware/routes/getRouteByIdMiddleware";
import updateRouteByIdMiddleware from "../middleware/routes/updateRouteByIdMiddleware";

export default function configureStore(preloadedState) {
  const middlewares = [
    getCollectionsMiddleware,
    addNewCollectionMiddleware,
    deleteCollectionMiddleware,
    updateCollectionMiddleware,
    getRoutesByCollectionNameMiddleware,
    deleteRouteByIdMiddleware,
    createNewRouteMiddleware,
    getRouteByIdMiddleware,
    updateRouteByIdMiddleware,
  ];
  const middlewareEnhancer = applyMiddleware(...middlewares);

  const enhancers = [middlewareEnhancer];
  const composedEnhancers = composeWithDevTools(...enhancers);

  const store = createStore(rootReducer, preloadedState, composedEnhancers);

  return store;
}
