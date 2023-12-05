import React, { useState } from "react";
import { useSelector } from "react-redux";
import AddNewCollectionTab from "./AddNewCollectionTab";
import RouteItem from "./RouteItem";
import CollectionList from "./CollectionList";

function Collection({ currentCollectionId }) {
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
        closeHandle={() => setNewMod(false)}
        currentCollectionId={currentCollectionId}
      />
    );
  }

  return (
    <CollectionList
      collectionName={currentCollection.name}
      currentRoutes={currentRoutes}
      btnAction={() => setNewMod(true)}
    />
  );
}

export default Collection;
