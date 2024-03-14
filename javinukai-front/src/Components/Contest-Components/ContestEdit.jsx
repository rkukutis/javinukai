import React, { useState } from "react";
import axios from "axios";

function EditContest({ contestDTO }) {
  const [contestName, setContestName] = useState(contestDTO?.name || "");
  const [error, setError] = useState("");

  const handleContestNameChange = (e) => {
    setContestName(e.target.value);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      if (!contestDTO || !contestDTO.id) {
        throw new Error("Invalid contest data");
      }
      const response = await axios.patch(
        `/api/v1/contests/${contestDTO.id}`, 
        { name: name }, 
        { withCredentials: true }
      );
      console.log("Contest edited successfully:", response.data);
    } catch (error) {
      console.error("Error editing contest:", error);
      setError("Failed to edit contest. Please try again later.");
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 p-6 bg-white rounded-md shadow-md">
      <h2 className="text-2xl font-semibold mb-4">Edit Contest</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label htmlFor="name" className="block text-sm font-medium text-gray-700">
            Contest Name:
          </label>
          <input
            type="text"
            id="name"
            value={name}
            onChange={handleContestNameChange}
            className="mt-1 p-2 w-full border-gray-300 rounded-md focus:outline-none focus:border-blue-500"
          />
        </div>
        {error && <p className="text-red-500">{error}</p>}
        <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">
          Save Changes
        </button>
      </form>
    </div>
  );
}

export default EditContest;
