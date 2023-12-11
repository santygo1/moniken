const initialState = {
  routes: [],
};

const routesReducer = (state = initialState, action = {}) => {
  switch (action.type) {
    case "setCurrentDetailRoute": {
      if (action.payload.loading) {
        console.log("Loading some detail route");
      }
      if (action.payload.error) {
        console.log(`Error in ${action.type}: ${action.payload.error}`);
      }
      if (!action.payload.error && action.payload.data) {
        return { ...state, currentRoute: action.payload.data };
      }
      return { ...state };
    }
    case "addRoutes": {
      if (action.payload.loading) {
        console.log("Loading routes");
      }
      if (action.payload.error) {
        console.log(`Error in ${action.type}: ${action.payload.error}`);
      }
      if (!action.payload.error && action.payload.data) {
        const routes = [];
        routes.push(...action.payload.data);
        return { ...state, routes };
      }
      return { ...state };
    }
    case "addRoute": {
      if (action.payload.loading) {
        console.log("Loading [create new route]");
      }
      if (action.payload.error) {
        console.log(`Error in ${action.type}: ${action.payload.error}`);
      }
      if (!action.payload.error && action.payload.data) {
        const routes = state.routes ? [...state.routes] : [];
        routes.push({ ...action.payload.data });
        return { ...state, routes };
      }
      return { ...state };
    }
    case "deleteRoute": {
      if (action.payload.error) {
        console.log(`Error in ${action.type}: ${action.payload.error}`);
      } else {
        let routes = state.routes ? [...state.routes] : [];
        routes = routes.filter((item) => item.id !== action.payload.id);
        return { ...state, routes };
      }
      return { ...state };
    }
    case "updateRoute": {
      if (action.payload.loading) {
        console.log("Loading [create new route]");
      }
      if (action.payload.error) {
        console.log(`Error in ${action.type}: ${action.payload.error}`);
      }
      if (!action.payload.error && action.payload.data) {
        let routes = state.routes ? [...state.routes] : [];
        routes = routes.filter((item) => item.id !== action.payload.data.id);
        routes.push({ ...action.payload.data });
        return { ...state, routes };
      }
      return { ...state };
    }
    default:
      return { ...state };
  }
};

export default routesReducer;
