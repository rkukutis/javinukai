import { useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import confirmEmail from "../services/confirmEmail";

function ConfirmEmailPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  console.log(searchParams.get("token"));

  useEffect(
    function () {
      if (!searchParams.get("token")) return;
      const res = confirmEmail(searchParams.get("token"));
      console.log(res);
    },
    [searchParams]
  );

  return <div>YOUR EMAIL HAS BEEN CONFIRMED</div>;
}

export default ConfirmEmailPage;
