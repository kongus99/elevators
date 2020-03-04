module IntSet = Belt.Set.Int;
[@bs.val] external fetch: string => Js.Promise.t('a) = "fetch";

let callFloor = (floor, _event) => {
  Js.Promise.(
    fetch(Constants.callUrl(floor))
    |> then_(_response => {Js.Promise.resolve()})
    |> catch(_err => {Js.Promise.resolve()})
    |> ignore
  );
};

[@react.component]
let make = () => {
  <Keypad
    stops=IntSet.empty
    floors=Constants.maxFloors
    onButtonPress=callFloor
  />;
};
