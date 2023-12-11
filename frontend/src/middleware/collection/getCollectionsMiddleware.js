import { getAllCollectionsRequest } from "../../api/collections/collectionsApi";

const getCollectionsMiddleware = (store) => (next) => (action) => {
  if (action.type === "getAllCollections") {
    (async () => {
      try {
        store.dispatch({
          type: "createManyColl",
          payload: {
            loading: true,
            data: null,
            error: null,
          },
        });
        const response = await getAllCollectionsRequest();
        store.dispatch({
          type: "createManyColl",
          payload: {
            loading: false,
            error: null,
            data: response.data,
          },
        });
      } catch (err) {
        store.dispatch({
          type: "createManyColl",
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
export default getCollectionsMiddleware;
