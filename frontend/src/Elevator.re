open Bootstrap;
module IntSet = Belt.Set.Int;
[@bs.val] external fetch: string => Js.Promise.t('a) = "fetch";

type direction =
  | NONE
  | UP
  | DOWN;

type state = {
  id: int,
  floor: int,
  direction,
  stops: IntSet.t,
};

let defaultState = {id: 1, floor: 0, direction: NONE, stops: IntSet.empty};

let toDirection = d =>
  switch (d) {
  | "UP" => UP
  | "DOWN" => DOWN
  | _ => NONE
  };

let decode = json =>
  Json.Decode.{
    id: json |> field("id", int),
    floor: json |> field("floor", int),
    direction: json |> field("direction", map(toDirection, string)),
    stops: json |> field("stops", array(int) |> map(IntSet.fromArray)),
  };

let rideFloor = (id, floor, _event) => {
  Js.Promise.(
    fetch(Constants.rideUrl(id, floor))
    |> then_(_response => {Js.Promise.resolve()})
    |> catch(_err => {Js.Promise.resolve()})
    |> ignore
  );
};

[@react.component]
let make = (~state=defaultState) => {
  <>
    <ProgressBar
      min=0
      max={Constants.maxFloors - 1}
      now={state.floor}
      animated={state.direction != NONE}
      label={string_of_int(state.floor)}
    />
    <Keypad
      stops={state.stops}
      floors=Constants.maxFloors
      onButtonPress={rideFloor(state.id)}
    />
  </>;
};
