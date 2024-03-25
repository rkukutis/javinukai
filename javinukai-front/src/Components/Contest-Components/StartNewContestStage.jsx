import axios from "axios";

const StartNewContestStage = (contestId) => {
  if (!confirm("qqq?")) return;

  axios
    .patch(
      `${import.meta.env.VITE_BACKEND}/api/v1/contests/${contestId}/reset`,
      {},
      { withCredentials: true }
    )
    .then((response) => {
      console.log("reset status log", response);
    })
    .catch((error) => {
      console.log(error);
    });
};
export default StartNewContestStage;
