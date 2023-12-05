import React from "react";
import RouteItem from "./RouteItem";
import CollectionListBtn from "./UI/CollectionListBtn";

function CollectionList({ collectionName, currentRoutes, btnAction }) {
  return (
    <div className="collection container px-4 py-5" id="featured-3">
      <h2 className="pb-2 border-bottom">{collectionName}</h2>
      <div className="row g-xl-5 py-5 row-cols-1 row-cols-lg-3">
        {currentRoutes?.map((route) => (
          <RouteItem
            key={route.id}
            name={route.name}
            method={route.method}
            endpoint={route.endpoint}
          />
        ))}
        <CollectionListBtn
          description="Add new route"
          onClickHandler={btnAction}
        />
      </div>
    </div>
  );
}

export default CollectionList;
