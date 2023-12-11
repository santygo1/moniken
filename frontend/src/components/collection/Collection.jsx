import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import RouteCreationView from "./RouteCreationView";
import CollectionList from "./CollectionList";
import RouteFullView from "./RouteFullView";

function Collection({ currentCollectionId, detailId, setDetailIdHandler }) {
  const collections = useSelector(
    (state) => state.collectionReducer.collections,
  );
  const currentCollection = collections.find(
    (item) => item.id === currentCollectionId,
  );

  const routes = useSelector((state) => state.routesReducer.routes);

  const dispatch = useDispatch();
  useEffect(() => {
    if (currentCollection) {
      dispatch({
        type: "getRoutesByCollectionName",
        payload: currentCollection.name,
      });
    }
  }, [currentCollection, dispatch]);

  // const currentRoutes = routes?.filter(
  //   (item) => item.collectionId === currentCollectionId,
  // );

  const [isNewModOn, setNewMod] = useState(false);
  const [isEditMod, setEditMod] = useState(false);

  if (!currentCollection) {
    return (
      <div className="collection container px-4 py-5">
        <h2 className="pb-2 border-bottom">
          Выберете коллекцию или создайте новую
        </h2>
      </div>
    );
  }

  if (isNewModOn) {
    return (
      <RouteCreationView
        initialValues={{
          name: "",
          endpoint: "",
          status: "",
          body: [{ name: "", value: "" }],
          headers: [{ name: "", value: "" }],
          timeout: "",
          description: "",
          method: "",
        }}
        dispatchType="createNewRoute"
        closeHandle={() => setNewMod(false)}
        collectionName={currentCollection.name}
      />
    );
  }
  // Edit mod
  if (isEditMod) {
    return (
      <RouteCreationView
        initialValues={routes.find((item) => item.id === detailId)}
        dispatchType="updateRouteById"
        closeHandle={() => setEditMod(false)}
        currentCollectionId={currentCollectionId}
      />
    );
  }

  if (detailId !== "") {
    return (
      <RouteFullView
        closeModHandler={() => {
          setDetailIdHandler("");
        }}
        editModOnHandler={() => setEditMod(true)}
        routeId={detailId}
      />
    );
  }

  return (
    <CollectionList
      collectionName={currentCollection.name}
      currentRoutes={routes}
      btnAction={() => setNewMod(true)}
      currentIdHandler={setDetailIdHandler}
    />
  );
}

export default Collection;
