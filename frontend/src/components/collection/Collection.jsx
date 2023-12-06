import React, { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import AddNewCollectionTab from "./AddNewCollectionTab";
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
  const currentRoutes = routes?.filter(
    (item) => item.collectionId === currentCollectionId,
  );

  const [isNewModOn, setNewMod] = useState(false);
  const [isEditMod, setEditMod] = useState(false);

  if (!currentCollection) {
    return (
      <div className="collection container px-4 py-5">
        <h2 className="pb-2 border-bottom">Выберете коллекцию</h2>
      </div>
    );
  }

  if (isNewModOn) {
    return (
      <AddNewCollectionTab
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
        dispatchType="createRout"
        closeHandle={() => setNewMod(false)}
        currentCollectionId={currentCollectionId}
      />
    );
  }
  // Edit mod
  if (isEditMod) {
    return (
      <AddNewCollectionTab
        initialValues={currentRoutes.find((item) => item.id === detailId)}
        dispatchType="updateRoute"
        closeHandle={() => setEditMod(false)}
        currentCollectionId={currentCollectionId}
      />
    );
  }

  if (detailId !== "") {
    return (
      <RouteFullView
        routeObject={currentRoutes.find((item) => item.id === detailId)}
        closeModHandler={() => {
          setDetailIdHandler("");
        }}
        editModOnHandler={() => setEditMod(true)}
      />
    );
  }

  return (
    <CollectionList
      collectionName={currentCollection.name}
      currentRoutes={currentRoutes}
      btnAction={() => setNewMod(true)}
      currentIdHandler={setDetailIdHandler}
    />
  );
}

export default Collection;
