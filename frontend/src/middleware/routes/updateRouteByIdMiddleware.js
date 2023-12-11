import { updateRouteById } from "../../api/routes/routesApi";
import { transformFormData } from "../../utils/formData";

const updateRouteByIdMiddleware = (store) => (next) => (action) => {
  if (action.type === "updateRouteById") {
    const correctData = transformFormData(action.payload);
    (async () => {
      try {
        store.dispatch({
          type: "updateRoute",
          payload: {
            loading: true,
            data: null,
            error: null,
          },
        });
        console.log("update data:", correctData);
        const response = await updateRouteById(action.routeId, correctData);
        store.dispatch({
          type: "updateRoute",
          payload: {
            loading: false,
            error: null,
            data: response.data,
          },
        });
      } catch (err) {
        store.dispatch({
          type: "updateRoute",
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

export default updateRouteByIdMiddleware;
