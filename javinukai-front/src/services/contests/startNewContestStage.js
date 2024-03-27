import axios from "axios";

const startNewContestStage = (contestId) => {
  axios
    .patch(
      `${import.meta.env.VITE_BACKEND}/api/v1/contests/${contestId}/reset`,
      {},
      { withCredentials: true }
    )
    .then(() => {})
    .catch((error) => {
      console.log(error);
    });
};
export default startNewContestStage;
