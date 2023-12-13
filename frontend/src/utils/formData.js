const bodyHeadTransformer = (object) => {
  return object.reduce((acc, obj) => {
    if (obj.name !== "" && obj.value !== "") {
      acc[obj.name] = obj.value;
    }
    return acc;
  }, {});
};

const bodyHeadToArray = (object) => {
  return Object.entries(object).map(([name, value]) => ({ name, value }));
};

const transformFormData = (data) => {
  const newBody = bodyHeadTransformer(data.body);
  const newHead = bodyHeadTransformer(data.headers);
  return {
    ...data,
    body: newBody,
    headers: newHead,
  };
};

const expandInitialValues = (data) => {
  const newBody = data.body ? bodyHeadToArray(data.body) : [];
  const newHead = data.headers ? bodyHeadToArray(data.headers) : [];
  const status = data.status ? data.status : "";
  const timeout = data.timeout ? data.timeout : "";
  const description = data.description ? data.description : "";

  return {
    ...data,
    body: newBody,
    headers: newHead,
    description,
    status,
    timeout,
  };
};

export {
  transformFormData,
  expandInitialValues,
  bodyHeadToArray,
  bodyHeadTransformer,
};
