import axios from "axios";

const getAllCollectionsRequest = async () => {
  return await axios.get("/moniken/collections");
};

const addNewCollectionRequest = async (data) => {
  return await axios.post("/moniken/collections", data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

const deleteCollectionRequest = async (collectionName) => {
  return await axios.delete(`/moniken/collections/${collectionName}`);
};

const fullUpdateCollectionRequest = async (collectionName, data) => {
  return await axios.put(`/moniken/collections/${collectionName}`, data, {
    headers: {
      "Content-Type": "application/json",
    },
  });
};

export {
  getAllCollectionsRequest,
  addNewCollectionRequest,
  deleteCollectionRequest,
  fullUpdateCollectionRequest,
};
