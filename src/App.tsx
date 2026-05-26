import { Link, Route, Routes } from 'react-router-dom'
import Home from './pages/Home'

function Menu() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Menu</h1>
        <Link
          to="/"
          className="text-sm font-semibold text-green-700 hover:text-green-900"
        >
          Back to home
        </Link>
      </div>
      <p className="text-slate-600">
        Explore a curated catalog of healthy meals designed for fitness and recovery.
      </p>
      <div className="grid gap-4 md:grid-cols-2">
        {['Fresh Bowls', 'Power Smoothies', 'Protein Salads', 'Recovery Meals'].map(
          (item) => (
            <div key={item} className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
              <h2 className="text-xl font-semibold text-slate-900">{item}</h2>
              <p className="mt-2 text-slate-600">Delicious and balanced options for your daily nutrition.</p>
            </div>
          ),
        )}
      </div>
    </div>
  )
}

function Coaching() {
  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Coaching</h1>
        <Link
          to="/"
          className="text-sm font-semibold text-green-700 hover:text-green-900"
        >
          Back to home
        </Link>
      </div>
      <p className="text-slate-600">
        Personalized fitness and meal coaching using AI-powered recommendations.
      </p>
      <div className="rounded-2xl border border-slate-200 bg-white p-6 shadow-sm">
        <h2 className="text-2xl font-semibold text-slate-900">Your AI Coaching Hub</h2>
        <p className="mt-3 text-slate-600">
          Get custom nutrition advice, workout suggestions, and progress tracking with real-time insights.
        </p>
      </div>
    </div>
  )
}

export default function App() {
  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <header className="border-b border-slate-200 bg-white/90 backdrop-blur-sm">
        <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-4">
          <Link to="/" className="text-xl font-bold text-slate-900">
            Protein Theory
          </Link>
          <nav className="flex items-center gap-4 text-sm font-semibold text-slate-700">
            <Link to="/menu" className="hover:text-slate-900">
              Menu
            </Link>
            <Link to="/coaching" className="hover:text-slate-900">
              Coaching
            </Link>
          </nav>
        </div>
      </header>
      <main className="mx-auto max-w-6xl px-4 py-10">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/menu" element={<Menu />} />
          <Route path="/coaching" element={<Coaching />} />
        </Routes>
      </main>
    </div>
  )
}
