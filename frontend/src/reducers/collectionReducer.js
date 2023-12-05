import { v4 } from "uuid";

const initialState = {
  collections: [],
};

const collectionReducer = (state = initialState, action = {}) => {
  switch (action.type) {
    case "createColl": {
      const collections = state.collections ? [...state.collections] : [];
      const existing = collections.findIndex(
        (item) => item.name === action.payload.name,
      );
      if (existing === -1) {
        collections.push({ ...action.payload, id: v4() });
      }
      return { ...state, collections };
    }
    default:
      return { ...state };
  }
};

export default collectionReducer;
