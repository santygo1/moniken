import { addNewCollectionRequest } from "../../api/collections/collectionsApi";

const addNewCollectionMiddleware = (store) => (next) => (action) => {
  if (action.type === "createColl") {
    (async () => {
      try {
        store.dispatch({
          type: "createCollection",
          payload: {
            loading: true,
            data: null,
            error: null,
          },
        });
        const response = await addNewCollectionRequest(action.payload);
        store.dispatch({
          type: "createCollection",
          payload: {
            loading: false,
            error: null,
            data: response.data,
          },
        });
      } catch (err) {
        store.dispatch({
          type: "createCollection",
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

export default addNewCollectionMiddleware;
