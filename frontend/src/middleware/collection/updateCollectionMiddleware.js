import { fullUpdateCollectionRequest } from "../../api/collections/collectionsApi";

const updateCollectionMiddleware = (store) => (next) => (action) => {
  if (action.type === "updateCollectionByName") {
    (async () => {
      try {
        const response = fullUpdateCollectionRequest(
          action.payload.name,
          action.payload.data,
        ); ///
        store.dispatch({
          type: "updateCollection",
          payload: {
            name: action.payload.name,
            data: response.data,
            error: null,
          },
        });
      } catch (err) {
        store.dispatch({
          type: "updateCollection",
          payload: {
            error: err,
          },
        });
      }
    })();
  }
  return next(action);
};

export default updateCollectionMiddleware;
