import { Link } from 'react-router-dom'

export default function Home() {
  return (
    <div className="space-y-12">
      <section className="bg-gradient-to-r from-green-500 to-green-700 text-white rounded-lg p-12 text-center">
        <h2 className="text-4xl font-bold mb-4">Welcome to Protein Theory</h2>
        <p className="text-xl mb-8 opacity-90">
          Gourmet health cafe offering personalized nutrition and fitness coaching
        </p>
        <div className="flex gap-4 justify-center flex-wrap">
          <Link
            to="/menu"
            className="bg-white text-green-600 px-8 py-3 rounded-lg font-bold hover:bg-gray-100 transition"
          >
            Explore Menu
          </Link>
          <Link
            to="/coaching"
            className="bg-green-900 text-white px-8 py-3 rounded-lg font-bold hover:bg-green-800 transition border border-white"
          >
            Get Coaching
          </Link>
        </div>
      </section>

      <section className="grid grid-cols-1 md:grid-cols-3 gap-8">
        {[
          {
            icon: '🍽️',
            title: 'Gourmet Meals',
            description: 'Carefully crafted meals designed for fitness and health',
          },
          {
            icon: '⭐',
            title: 'Loyalty Rewards',
            description: 'Earn points on every order and unlock exclusive benefits',
          },
          {
            icon: '🏋️',
            title: 'AI Coaching',
            description: 'Personalized fitness tips and meal recommendations via AI',
          },
        ].map((feature) => (
          <div
            key={feature.title}
            className="bg-white p-6 rounded-lg shadow-md hover:shadow-lg transition"
          >
            <div className="text-5xl mb-4">{feature.icon}</div>
            <h3 className="text-xl font-bold mb-2">{feature.title}</h3>
            <p className="text-gray-600">{feature.description}</p>
          </div>
        ))}
      </section>

      <section className="bg-gray-100 p-8 rounded-lg">
        <h3 className="text-2xl font-bold mb-8 text-center">Why Choose Us</h3>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8 text-center">
          {[
            { number: '500+', label: 'Happy Customers' },
            { number: '50+', label: 'Menu Items' },
            { number: '100%', label: 'Fresh Ingredients' },
            { number: '24/7', label: 'AI Support' },
          ].map((stat) => (
            <div key={stat.label}>
              <div className="text-4xl font-bold text-green-600 mb-2">{stat.number}</div>
              <p className="text-gray-600">{stat.label}</p>
            </div>
          ))}
        </div>
      </section>

      <section className="bg-green-50 p-8 rounded-lg border-2 border-green-200 text-center">
        <h3 className="text-2xl font-bold mb-4">Ready to Start Your Fitness Journey?</h3>
        <p className="text-gray-600 mb-6">
          Join hundreds of fitness enthusiasts enjoying our premium health meals
        </p>
        <Link
          to="/menu"
          className="inline-block bg-green-600 text-white px-8 py-3 rounded-lg font-bold hover:bg-green-700 transition"
        >
          Order Now
        </Link>
      </section>
    </div>
  )
}
