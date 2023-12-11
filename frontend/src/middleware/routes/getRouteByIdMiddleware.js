import { getRouteById } from "../../api/routes/routesApi";

const getRouteByIdMiddleware = (store) => (next) => (action) => {
  if (action.type === "getRouteById") {
    (async () => {
      try {
        store.dispatch({
          type: "setCurrentDetailRoute",
          payload: {
            loading: true,
            data: null,
            error: null,
          },
        });
        const response = await getRouteById(action.payload);
        store.dispatch({
          type: "setCurrentDetailRoute",
          payload: {
            loading: false,
            error: null,
            data: response.data,
          },
        });
      } catch (err) {
        store.dispatch({
          type: "setCurrentDetailRoute",
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

export default getRouteByIdMiddleware;
