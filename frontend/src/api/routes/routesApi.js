import axios from "axios";

const getRoutesByCollectionNameApi = async (collectionName) => {
  return await axios.get(`/moniken/collections/${collectionName}/routes`);
};

const deleteRouteById = async (routeId) => {
  return await axios.delete(`moniken/routes/${routeId}`);
};

const createNewRoute = async (collectionName, data) => {
  return await axios.post(
    `/moniken/collections/${collectionName}/routes`,
    data,
    {
      headers: {
        "Content-Type": "application/json",
      },
    },
  );
};

const getRouteById = async (routeId) => {
  return await axios.get(`/moniken/routes/${routeId}`);
};

const updateRouteById = async (routeId, data) => {
  return await axios.put(`/moniken/routes/${routeId}`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

export {
  getRoutesByCollectionNameApi,
  deleteRouteById,
  createNewRoute,
  getRouteById,
  updateRouteById,
};
