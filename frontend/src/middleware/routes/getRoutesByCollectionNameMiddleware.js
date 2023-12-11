import { getRoutesByCollectionNameApi } from "../../api/routes/routesApi";

const getRoutesByCollectionNameMiddleware = (store) => (next) => (action) => {
  if (action.type === "getRoutesByCollectionName") {
    (async () => {
      try {
        store.dispatch({
          type: "addRoutes",
          payload: {
            loading: true,
            data: null,
            error: null,
          },
        });
        const response = await getRoutesByCollectionNameApi(action.payload);
        store.dispatch({
          type: "addRoutes",
          payload: {
            loading: false,
            error: null,
            data: response.data,
          },
        });
      } catch (err) {
        store.dispatch({
          type: "addRoutes",
          payload: {
            loading: false,
            error: err,
            data: null,
          },
        });
      }
    })();
  }
  return next(action);
};

export default getRoutesByCollectionNameMiddleware;
