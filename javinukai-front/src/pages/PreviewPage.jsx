import React, { useState } from "react";
import PreviewCategories from "../Components/Contest-Components/PreviewCategories";
import ContestPreview from "../Components/Contest-Components/ContestPreview";

function PreviewPage() {
  const [showCategories, setShowCategories] = useState(false);
  const [showContests, setShowContests] = useState(false);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-semibold mb-4">Preview Page</h2>

      <div className="mb-4">
        <button onClick={() => setShowCategories(!showCategories)} className={`w-full px-4 py-2 rounded-md focus:outline-none ${showCategories ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-700'}`}>
          {showCategories ? 'Hide Categories' : 'Show Categories'}
        </button>
        {showCategories && <PreviewCategories />}
      </div>

      <div className="mb-4">
        <button onClick={() => setShowContests(!showContests)} className={`w-full px-4 py-2 rounded-md focus:outline-none ${showContests ? 'bg-blue-500 text-white' : 'bg-gray-200 text-gray-700'}`}>
          {showContests ? 'Hide Contests' : 'Show Contests'}
        </button>
        {showContests && <ContestPreview />}
      </div>
    </div>
  );
}

export default PreviewPage;
