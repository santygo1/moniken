import { v4 } from "uuid";

const initialState = {
  routes: [],
};

const routesReducer = (state = initialState, action = {}) => {
  switch (action.type) {
    case "createRout": {
      const routes = state.routes ? [...state.routes] : [];
      routes.push({ ...action.payload, id: v4() });
      return { ...state, routes };
    }
    case "deleteRout": {
      let routes = state.routes ? [...state.routes] : [];
      routes = routes.filter((item) => item.id !== action.payload.id);
      return { ...state, routes };
    }
    case "updateRoute": {
      let routes = state.routes ? [...state.routes] : [];
      routes = routes.filter((item) => item.id !== action.payload.id);
      routes.push({ ...action.payload });
      return { ...state, routes };
    }
    default:
      return { ...state };
  }
};

export default routesReducer;
