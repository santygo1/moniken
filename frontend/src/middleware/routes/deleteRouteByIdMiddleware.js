import { deleteRouteById } from "../../api/routes/routesApi";

const deleteRouteByIdMiddleware = (store) => (next) => (action) => {
  if (action.type === "deleteRouteById") {
    (async () => {
      try {
        const response = await deleteRouteById(action.payload);
        store.dispatch({
          type: "deleteRoute",
          payload: {
            error: null,
            id: action.payload,
          },
        });
      } catch (err) {
        store.dispatch({
          type: "deleteRoute",
          payload: {
            error: err,
            data: null,
          },
        });
      }
    })();
  }
  return next(action);
};

export default deleteRouteByIdMiddleware;
