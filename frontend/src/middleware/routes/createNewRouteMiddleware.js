import { transformFormData } from "../../utils/formData";
import { createNewRoute } from "../../api/routes/routesApi";

const createNewRouteMiddleware = (store) => (next) => (action) => {
  if (action.type === "createNewRoute") {
    const correctData = transformFormData(action.payload);
    (async () => {
      try {
        store.dispatch({
          type: "addRoute",
          payload: {
            loading: true,
            data: null,
            error: null,
          },
        });
        const response = await createNewRoute(action.collection, correctData);
        store.dispatch({
          type: "addRoute",
          payload: {
            loading: false,
            error: null,
            data: response.data,
          },
        });
      } catch (err) {
        store.dispatch({
          type: "addRoute",
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

export default createNewRouteMiddleware;
