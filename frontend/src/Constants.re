let maxFloors = 10;

let rideUrl = (id, floor) =>
  "http://localhost:8080/rest/v1/ride?elevatorId="
  ++ string_of_int(id)
  ++ "&floor="
  ++ string_of_int(floor);

let callUrl = floor =>
  "http://localhost:8080/rest/v1/call?floor=" ++ string_of_int(floor);
