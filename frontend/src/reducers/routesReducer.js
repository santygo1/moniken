import { v4 } from "uuid";

const initialState = {
  routes: [],
};

const routesReducer = (state = initialState, action = {}) => {
  switch (action.type) {
    case "create": {
      const routes = state.routes ? [...state.routes] : [];

      return { ...state, routes };
    }
    default:
      return { ...state };
  }
};

export default routesReducer();
