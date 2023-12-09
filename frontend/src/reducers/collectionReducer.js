const initialState = {
  collections: [],
};

const collectionReducer = (state = initialState, action = {}) => {
  switch (action.type) {
    case "createManyColl": {
      if (action.payload.loading) {
        console.log("Collection loading...");
      }
      if (action.payload.data && !action.payload.error) {
        let collections = state.collections ? [...state.collections] : [];
        collections = collections.concat(action.payload.data);
        return {
          ...state,
          collections,
        };
      }
      if (action.payload.error) {
        console.log(`Error in ${action.type}: `, action.payload.error);
      }
      return { ...state };
    }
    case "createCollection": {
      if (action.payload.loading) {
        console.log("Collection add loading...");
      }
      if (action.payload.error) {
        console.log(`Error in ${action.type}: `, action.payload.error);
      }
      if (action.payload.data && !action.payload.error) {
        const collections = state.collections ? [...state.collections] : [];
        const existing = collections.findIndex(
          (item) => item.name === action.payload.name,
        );
        if (existing === -1) {
          collections.push({ ...action.payload.data });
        }
        return { ...state, collections };
      }
      return { ...state };
    }
    case "deleteCollection": {
      if (action.payload.error !== null) {
        console.log(`Error in ${action.type}: `, action.payload.error);
      } else {
        let collections = state.collections ? [...state.collections] : [];
        collections = collections.filter(
          (item) => item.name !== action.payload.data,
        );
        return { ...state, collections };
      }
      return { ...state };
    }
    case "updateCollection": {
      if (action.payload.error !== null) {
        console.log(`Error in ${action.type}: `, action.payload.error);
      } else {
        let collections = state.collections ? [...state.collections] : [];
        collections = collections.filter(
          (item) => item.name !== action.payload.name,
        );
        collections.push({ ...action.payload.data });
        return { ...state, collections };
      }
      return { ...state };
    }
    default:
      return { ...state };
  }
};

export default collectionReducer;
