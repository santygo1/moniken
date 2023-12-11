import { deleteCollectionRequest } from "../../api/collections/collectionsApi";

const deleteCollectionMiddleware = (store) => (next) => (action) => {
  if (action.type === "deleteCollectionByName") {
    (async () => {
      try {
        await deleteCollectionRequest(action.payload);
        store.dispatch({
          type: "deleteCollection",
          payload: {
            data: action.payload,
            error: null,
          },
        });
      } catch (err) {
        store.dispatch({
          type: "deleteCollection",
          payload: {
            error: err,
          },
        });
      }
    })();
  }
  return next(action);
};

export default deleteCollectionMiddleware;
